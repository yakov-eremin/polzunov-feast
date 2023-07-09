using FirebaseAdmin.Messaging;
using FirebaseAdmin;
using Google.Apis.Auth.OAuth2;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Push_server_app
{
    public class FireBaseServices
    {
        private MulticastMessage message = new MulticastMessage()
        {
            Tokens = new List<string>(),// Список токенов устройств, на которые нужно отправить сообщение       
            //Содержание уведомления
            Notification = new Notification
            {
                Title = "Заголовок уведомления",
                Body = "Текст уведомления"
            }
        };
       

        public async void ReadFireBaseAdminSdk() /*Чтение sdk для работы с сервером FCM*/
        {
            string filePath = @"C:\\polzunov-feast\\Push-server-app\\Push-server-app\feast-cd00c-firebase-adminsdk-blbam-fd102c9dce.json";

            FirebaseApp app = FirebaseApp.Create(new AppOptions()
            {
                Credential = GoogleCredential.FromFile(filePath)
            });
            if (app != null)
            {
                Console.WriteLine("Firebase приложение успешно создано.");
            }
            else
            {
                Console.WriteLine("Ошибка при создании Firebase приложения.");
            }
        }
        
        public void setNotificationParams(List<string> tokens, string title, string messageBody)
        {
            message.Tokens = tokens;
            message.Notification.Title = title;
            message.Notification.Body = messageBody;
        }
        
        public  async Task SendNotification()
        {
            var response = await FirebaseMessaging.DefaultInstance.SendMulticastAsync(message);
            if (response != null)
            {
                Console.WriteLine("Уведомление отправлено");
            }
        }
    }
}
