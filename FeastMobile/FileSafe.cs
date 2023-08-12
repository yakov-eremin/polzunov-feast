using Google.Cloud.Firestore;

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.IO;

using Microsoft.Maui.Controls.PlatformConfiguration;
using FileSystem = Xamarin.Essentials.FileSystem;
//using PdfSharpCore.Drawing;
//using PdfSharpCore.Pdf;
using Microsoft.Maui.Controls;
using Plugin.FilePicker.Abstractions;
using Plugin.FilePicker;
using CommunityToolkit.Maui.Storage;
using iTextSharp.text.pdf;
using iTextSharp.text;
using System.Reflection;
//using Microsoft.Maui.Essentials;




namespace FeastMobile
{
    public class FileSafe
    {
        public FileSafe() { }
       private IFileSaver fileSaver;
       public void FileSaverInit(IFileSaver fileSaver)
        {
            this.fileSaver = fileSaver;
        }
        /*Тестовый вариант, требует доработку по сохранению картинок*/
        public byte[] ConvertTextToPdf(string text)
        {
            var utf8Bytes = Encoding.UTF8.GetBytes(text);

            using (MemoryStream pdfStream = new MemoryStream())
            {
                using (var doc = new iTextSharp.text.Document())
                {
                    var writer = iTextSharp.text.pdf.PdfWriter.GetInstance(doc, pdfStream);
                    doc.Open();
                    doc.Add(new iTextSharp.text.Paragraph(Encoding.UTF8.GetString(utf8Bytes)));
                    doc.Close();
                }

                return pdfStream.ToArray();
            }
        }

        public async Task<string> SavePdfAsync(byte[] pdfBytes)
        {
            using (var pdfStream = new MemoryStream(pdfBytes))
            {
                var pdfPath = await fileSaver.SaveAsync("Route.pdf", pdfStream, CancellationToken.None);
                return pdfPath.ToString();
            }
        }


    }
}
