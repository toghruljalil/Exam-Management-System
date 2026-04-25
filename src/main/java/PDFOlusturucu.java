import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.WritableImage;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class PDFOlusturucu {

    public static int ogrenciSayisi = 0;

    public static void pdfOlustur(Scene javafxSahnesi, List<OturmaDuzeniModel> ogrenciListesi, String dosyaYolu) {
        try (PDDocument document = new PDDocument()) {

            PDFont fontNormal = null;
            PDFont fontBold = null;

            // Font yükleme
            try {
                File arialFont = new File("C:\\Windows\\Fonts\\arial.ttf");
                File arialBoldFont = new File("C:\\Windows\\Fonts\\arialbd.ttf");

                if (arialFont.exists()) {
                    fontNormal = PDType0Font.load(document, arialFont);
                    if (arialBoldFont.exists()) {
                        fontBold = PDType0Font.load(document, arialBoldFont);
                    } else {
                        fontBold = fontNormal;
                    }
                }
            } catch (Exception e) {
                System.err.println("Unicode font yüklenemedi: " + e.getMessage());
            }

            if (fontNormal == null) {
                fontNormal = PDType1Font.HELVETICA;
                fontBold = PDType1Font.HELVETICA_BOLD;
            }

            ogrenciSay(ogrenciListesi);

            // 1. Sayfa: JavaFX Sahnesi
            PDPage sayfa1 = new PDPage(PDRectangle.A4);
            document.addPage(sayfa1);
            sayfa1Ekle(document, sayfa1, javafxSahnesi, fontBold, fontNormal);

            // 2. Sayfa: Öğrenci Listesi
            PDPage sayfa2 = new PDPage(PDRectangle.A4);
            document.addPage(sayfa2);
            sayfa2Ekle(document, sayfa2, ogrenciListesi, fontBold, fontNormal);

            // PDF'yi kaydet
            document.save(dosyaYolu);
            System.out.println("PDF başarıyla oluşturuldu: " + dosyaYolu);

        } catch (IOException e) {
            System.err.println("PDF oluşturulurken hata oluştu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void sayfa1Ekle(PDDocument document, PDPage sayfa, Scene javafxSahnesi,
                                   PDFont fontBold, PDFont fontNormal) throws IOException {
        PDPageContentStream contentStream = new PDPageContentStream(document, sayfa);

        try {
            // Başlık
            contentStream.setFont(fontBold, 20);
            contentStream.beginText();
            contentStream.newLineAtOffset(50, 780);
            contentStream.showText("Sınıf Oturma Düzeni Görünümü");
            contentStream.endText();

            // Alt başlık
            contentStream.setFont(fontNormal, 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(50, 760);
            contentStream.showText("Oluşturulma Tarihi: " + java.time.LocalDate.now());
            contentStream.endText();

            // JavaFX sahnesini ekle
            if (javafxSahnesi != null) {
                WritableImage image = javafxSahnesi.snapshot(null);
                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);

                File tempFile = File.createTempFile("javafx_sahne", ".png");
                ImageIO.write(bufferedImage, "png", tempFile);

                PDImageXObject pdImage = PDImageXObject.createFromFile(tempFile.getAbsolutePath(), document);

                // Görseli ölçekle
                float pageWidth = PDRectangle.A4.getWidth() - 100;
                float scale = pageWidth / pdImage.getWidth();
                float imageHeight = pdImage.getHeight() * scale;

                float x = 50;
                float y = 750 - imageHeight - 20;

                contentStream.drawImage(pdImage, x, y, pdImage.getWidth() * scale, imageHeight);
                tempFile.delete();

                // Görsel altı açıklama
                contentStream.setFont(fontNormal, 10);
                contentStream.beginText();
                contentStream.newLineAtOffset(50, y - 20);
                contentStream.showText("Not: Yukarıdaki görsel sınıf oturma düzenini göstermektedir.");
                contentStream.endText();
            }

        } finally {
            contentStream.close();
        }
    }

    private static void sayfa2Ekle(PDDocument document, PDPage sayfa, List<OturmaDuzeniModel> ogrenciListesi,
                                   PDFont fontBold, PDFont fontNormal) throws IOException {
        PDPageContentStream contentStream = new PDPageContentStream(document, sayfa);

        try {
            // Başlık
            contentStream.setFont(fontBold, 20);
            contentStream.beginText();
            contentStream.newLineAtOffset(50, 780);
            contentStream.showText("Öğrenci Oturma Listesi");
            contentStream.endText();

            // Tablo başlıkları
            contentStream.setFont(fontBold, 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(50, 740);
            contentStream.showText("Sıra");
            contentStream.newLineAtOffset(40, 0);
            contentStream.showText("Öğrenci No");
            contentStream.newLineAtOffset(100, 0);
            contentStream.showText("Öğrenci Adı");
            contentStream.newLineAtOffset(150, 0);
            contentStream.showText("Derslik");
            contentStream.newLineAtOffset(80, 0);
            contentStream.showText("Sütun");
            contentStream.newLineAtOffset(50, 0);
            contentStream.showText("Satır");
            contentStream.endText();

            // Tablo çizgisi (başlık altı)
            contentStream.setLineWidth(1f);
            contentStream.moveTo(50, 735);
            contentStream.lineTo(PDRectangle.A4.getWidth() - 50, 735);
            contentStream.stroke();

            // Öğrenci verileri
            float yPozisyon = 720;
            int siraNo = 1;

            for (OturmaDuzeniModel ogrenci : ogrenciListesi) {
                if (yPozisyon < 50) {
                    // Yeni sayfa oluştur ve mevcut contentStream'i kapat
                    contentStream.close();
                    PDPage yeniSayfa = new PDPage(PDRectangle.A4);
                    document.addPage(yeniSayfa);
                    yeniSayfaEkle(document, yeniSayfa, ogrenciListesi.subList(siraNo - 1, ogrenciListesi.size()),
                            fontBold, fontNormal, siraNo);
                    return;
                }

                // Satır arka planı (zebra desen)
                if (siraNo % 2 == 0) {
                    contentStream.setNonStrokingColor(250, 250, 250);
                } else {
                    contentStream.setNonStrokingColor(255, 255, 255);
                }
                contentStream.addRect(50, yPozisyon - 15, PDRectangle.A4.getWidth() - 100, 20);
                contentStream.fill();
                contentStream.setNonStrokingColor(0, 0, 0);

                // Öğrenci verileri - FONT'U HER ZAMAN AYARLA
                contentStream.setFont(fontNormal, 10);

                // Sıra numarası
                contentStream.beginText();
                contentStream.newLineAtOffset(50, yPozisyon - 10);
                contentStream.showText(String.valueOf(siraNo) + ".");
                contentStream.endText();

                // Öğrenci bilgileri
                contentStream.beginText();
                contentStream.newLineAtOffset(70, yPozisyon - 10);
                contentStream.showText(ogrenci.getOgrencino() != null ? ogrenci.getOgrencino() : "");
                contentStream.newLineAtOffset(100, 0);
                contentStream.showText(ogrenci.getOgrenciadi() != null ? ogrenci.getOgrenciadi() : "");
                contentStream.newLineAtOffset(150, 0);
                contentStream.showText(ogrenci.getDerslik() != null ? ogrenci.getDerslik() : "");
                contentStream.newLineAtOffset(80, 0);
                contentStream.showText(String.valueOf(ogrenci.getSutun()));
                contentStream.newLineAtOffset(50, 0);
                contentStream.showText(String.valueOf(ogrenci.getSatir()));
                contentStream.endText();

                // Satır çizgisi
                contentStream.setLineWidth(0.5f);
                contentStream.moveTo(50, yPozisyon - 17);
                contentStream.lineTo(PDRectangle.A4.getWidth() - 50, yPozisyon - 17);
                contentStream.stroke();

                yPozisyon -= 20;
                siraNo++;
            }

            // Toplam öğrenci sayısı
            contentStream.setFont(fontBold, 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(50, yPozisyon - 30);
            contentStream.showText("Toplam Öğrenci Sayısı: " + PDFOlusturucu.ogrenciSayisi);
            contentStream.endText();

        } finally {
            contentStream.close();
        }
    }

    private static void yeniSayfaEkle(PDDocument document, PDPage sayfa, List<OturmaDuzeniModel> ogrenciListesi,
                                      PDFont fontBold, PDFont fontNormal, int baslangicSirasi) throws IOException {
        PDPageContentStream contentStream = new PDPageContentStream(document, sayfa);

        try {
            // Başlık
            contentStream.setFont(fontBold, 20);
            contentStream.beginText();
            contentStream.newLineAtOffset(50, 780);
            contentStream.showText("Öğrenci Oturma Listesi (Devam)");
            contentStream.endText();

            // Tablo başlıkları
            contentStream.setFont(fontBold, 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(50, 740);
            contentStream.showText("Sıra");
            contentStream.newLineAtOffset(40, 0);
            contentStream.showText("Öğrenci No");
            contentStream.newLineAtOffset(100, 0);
            contentStream.showText("Öğrenci Adı");
            contentStream.newLineAtOffset(150, 0);
            contentStream.showText("Derslik");
            contentStream.newLineAtOffset(80, 0);
            contentStream.showText("Sütun");
            contentStream.newLineAtOffset(50, 0);
            contentStream.showText("Satır");
            contentStream.endText();

            // Tablo çizgisi
            contentStream.setLineWidth(1f);
            contentStream.moveTo(50, 735);
            contentStream.lineTo(PDRectangle.A4.getWidth() - 50, 735);
            contentStream.stroke();

            // Öğrenci verileri
            float yPozisyon = 720;
            int siraNo = baslangicSirasi;

            for (OturmaDuzeniModel ogrenci : ogrenciListesi) {
                if (yPozisyon < 50) {
                    break;
                }

                // Satır arka planı
                if (siraNo % 2 == 0) {
                    contentStream.setNonStrokingColor(250, 250, 250);
                } else {
                    contentStream.setNonStrokingColor(255, 255, 255);
                }
                contentStream.addRect(50, yPozisyon - 15, PDRectangle.A4.getWidth() - 100, 20);
                contentStream.fill();
                contentStream.setNonStrokingColor(0, 0, 0);

                // Öğrenci verileri - FONT'U HER ZAMAN AYARLA
                contentStream.setFont(fontNormal, 10);

                // Sıra numarası
                contentStream.beginText();
                contentStream.newLineAtOffset(50, yPozisyon - 10);
                contentStream.showText(String.valueOf(siraNo) + ".");
                contentStream.endText();

                // Öğrenci bilgileri
                contentStream.beginText();
                contentStream.newLineAtOffset(70, yPozisyon - 10);
                contentStream.showText(ogrenci.getOgrencino() != null ? ogrenci.getOgrencino() : "");
                contentStream.newLineAtOffset(100, 0);
                contentStream.showText(ogrenci.getOgrenciadi() != null ? ogrenci.getOgrenciadi() : "");
                contentStream.newLineAtOffset(150, 0);
                contentStream.showText(ogrenci.getDerslik() != null ? ogrenci.getDerslik() : "");
                contentStream.newLineAtOffset(80, 0);
                contentStream.showText(String.valueOf(ogrenci.getSutun()));
                contentStream.newLineAtOffset(50, 0);
                contentStream.showText(String.valueOf(ogrenci.getSatir()));
                contentStream.endText();

                // Satır çizgisi
                contentStream.setLineWidth(0.5f);
                contentStream.moveTo(50, yPozisyon - 17);
                contentStream.lineTo(PDRectangle.A4.getWidth() - 50, yPozisyon - 17);
                contentStream.stroke();

                yPozisyon -= 20;
                siraNo++;
            }

            // Toplam öğrenci sayısı
            contentStream.setFont(fontBold, 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(50, yPozisyon - 30);
            contentStream.showText("Toplam Öğrenci Sayısı: " + PDFOlusturucu.ogrenciSayisi);
            contentStream.endText();

        } finally {
            contentStream.close();
        }
    }

    // Basit ve güvenli versiyon (önerilen)
    public static void pdfOlusturBasit(Scene javafxSahnesi, List<OturmaDuzeniModel> ogrenciListesi, String dosyaYolu) {
        try (PDDocument document = new PDDocument()) {

            PDFont font = PDType1Font.HELVETICA;
            PDFont fontBold = PDType1Font.HELVETICA_BOLD;

            // 1. Sayfa: JavaFX Sahnesi
            PDPage sayfa1 = new PDPage(PDRectangle.A4);
            document.addPage(sayfa1);

            PDPageContentStream contentStream1 = new PDPageContentStream(document, sayfa1);
            contentStream1.setFont(fontBold, 18);
            contentStream1.beginText();
            contentStream1.newLineAtOffset(50, 750);
            contentStream1.showText("JavaFX Sahne Goruntusu");
            contentStream1.endText();

            if (javafxSahnesi != null) {
                WritableImage image = javafxSahnesi.snapshot(null);
                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
                File tempFile = File.createTempFile("javafx_sahne", ".png");
                ImageIO.write(bufferedImage, "png", tempFile);
                PDImageXObject pdImage = PDImageXObject.createFromFile(tempFile.getAbsolutePath(), document);
                contentStream1.drawImage(pdImage, 50, 400, pdImage.getWidth() * 0.5f, pdImage.getHeight() * 0.5f);
                tempFile.delete();
            }
            contentStream1.close();

            // 2. Sayfa: Öğrenci Listesi
            PDPage sayfa2 = new PDPage(PDRectangle.A4);
            document.addPage(sayfa2);

            PDPageContentStream contentStream2 = new PDPageContentStream(document, sayfa2);

            // Başlık
            contentStream2.setFont(fontBold, 18);
            contentStream2.beginText();
            contentStream2.newLineAtOffset(50, 750);
            contentStream2.showText("Ogrenci Listesi");
            contentStream2.endText();

            // Tablo başlıkları
            contentStream2.setFont(fontBold, 12);
            contentStream2.beginText();
            contentStream2.newLineAtOffset(50, 700);
            contentStream2.showText("Ogrenci No");
            contentStream2.newLineAtOffset(100, 0);
            contentStream2.showText("Ogrenci Adi");
            contentStream2.newLineAtOffset(150, 0);
            contentStream2.showText("Derslik");
            contentStream2.newLineAtOffset(100, 0);
            contentStream2.showText("Sutun");
            contentStream2.newLineAtOffset(50, 0);
            contentStream2.showText("Satir");
            contentStream2.endText();

            // Öğrenci verileri
            contentStream2.setFont(font, 10);
            float yPozisyon = 680;

            for (OturmaDuzeniModel ogrenci : ogrenciListesi) {
                if (yPozisyon < 50) break;

                contentStream2.beginText();
                contentStream2.newLineAtOffset(50, yPozisyon);
                contentStream2.showText(ogrenci.getOgrencino() != null ? ogrenci.getOgrencino() : "");
                contentStream2.newLineAtOffset(100, 0);
                contentStream2.showText(ogrenci.getOgrenciadi() != null ? ogrenci.getOgrenciadi() : "");
                contentStream2.newLineAtOffset(150, 0);
                contentStream2.showText(ogrenci.getDerslik() != null ? ogrenci.getDerslik() : "");
                contentStream2.newLineAtOffset(100, 0);
                contentStream2.showText(String.valueOf(ogrenci.getSutun()));
                contentStream2.newLineAtOffset(50, 0);
                contentStream2.showText(String.valueOf(ogrenci.getSatir()));
                contentStream2.endText();

                yPozisyon -= 20;
            }

            contentStream2.close();
            document.save(dosyaYolu);
            System.out.println("PDF başarıyla oluşturuldu: " + dosyaYolu);

        } catch (IOException e) {
            System.err.println("PDF oluşturulurken hata oluştu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void ogrenciSay(List<OturmaDuzeniModel> ogrenciListesi)
    {
        PDFOlusturucu.ogrenciSayisi = ogrenciListesi.size();
    }
}