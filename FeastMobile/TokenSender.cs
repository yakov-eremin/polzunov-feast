using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Firebase.Database;
using Firebase.Database.Query;
//using ThreadNetwork;
using Xamarin.Essentials;
//using static UIKit.UIGestureRecognizer;

namespace FeastMobile
{
    public class TokenSender
    {
        //private static readonly HttpClient client = new HttpClient();
        public async Task SendTokenToServer(string deviceToken)
        {
            // Замените URL на URL, предоставленный ngrok для вашего локального сервера
            HttpClient client = new HttpClient();
            var ngrokUrl = "https://dc00-213-209-148-4.ngrok-free.app";
            var apiUrl = $"{ngrokUrl}/api/token" ;

            var content = new StringContent(deviceToken);

            try
            {
                var response = await client.PostAsync(apiUrl, content);
                response.EnsureSuccessStatusCode();
            }
            catch (Exception ex)
            {
                // Обработка ошибок при отправке запроса
                Console.WriteLine("Ошибка при отправке токена на сервер: " + ex.Message);
            }
        }
    }
}
