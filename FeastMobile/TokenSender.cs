using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Firebase.Database;
using Firebase.Database.Query;
using Xamarin.Essentials;


namespace FeastMobile
{
    public class TokenSender
    {

        public async Task SendTokenToServer(string deviceToken)
        {
            // Замените URL на URL, предоставленный ngrok для вашего локального сервера
            HttpClient client = new HttpClient();
            var ngrokUrl = "https://3213-2a0c-f040-0-8-00-18.ngrok-free.app";
            var apiUrl = $"{ngrokUrl}/api/token";

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

        /*Тестовый метод, в дальнейшем удалить*/
        public async Task TestRequest(string deviceToken, string title, string text)
        {
            // Замените URL на URL, предоставленный ngrok для вашего локального сервера
            HttpClient client = new HttpClient();
            var ngrokUrl = "https://3213-2a0c-f040-0-8-00-18.ngrok-free.app";
            var apiUrl = $"{ngrokUrl}/api/message";

            try
            {
                // Создаем объект с параметрами
                var requestData = new
                {
                    DeviceToken = deviceToken,
                    Title = title,
                    Text = text
                };

                // Сериализуем объект в JSON
                var jsonRequestData = Newtonsoft.Json.JsonConvert.SerializeObject(requestData);
                var content = new StringContent(jsonRequestData, Encoding.UTF8, "application/json");

                // Отправляем HTTP POST запрос
                var response = await client.PostAsync(apiUrl, content);
                response.EnsureSuccessStatusCode();
            }
            catch (Exception ex)
            {
                // Обработка ошибок при отправке запроса
                Console.WriteLine("Ошибка при отправке запроса на сервер: " + ex.Message);
            }
        }


    }
}
