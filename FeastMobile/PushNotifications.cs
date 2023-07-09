using FirebaseAdmin.Messaging;
using FirebaseAdmin;
using Google.Apis.Auth.OAuth2;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Google.Cloud.Firestore.V1;
using Google.Cloud.Firestore;
using Firebase.Database;
using Firebase.Database.Query;
//using AVFoundation;

namespace FeastMobile
{
   public class PushNotifications
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
            var stream = await FileSystem.OpenAppPackageFileAsync("feast-cd00c-firebase-adminsdk-blbam-0e8aef73a7.json");
            var reader = new StreamReader(stream);
            var jsonContent = reader.ReadToEnd();
            FirebaseApp firebaseApp;
            if (FirebaseMessaging.DefaultInstance == null)
            {
                 firebaseApp= FirebaseApp.Create(new AppOptions()
                {
                    Credential = GoogleCredential.FromJson(jsonContent)
                });
                
            }
            
        }
        public  void CreateTokensDataBase()
        { 

        }


        public void setNotificationParams(List<string> tokens, string title, string messageBody)
        {
            message.Tokens = tokens;
            message.Notification.Title = title;
            message.Notification.Body = messageBody;
        }
        public string getNotificationParams() { return message.Tokens[0]; }
        public async void SendNotification()
        {
            var response = await FirebaseMessaging.DefaultInstance.SendMulticastAsync(this.message);
            if (response != null)
            {
                await App.Current.MainPage.DisplayAlert("Уведомление отправлено", "Уведомление успешно отправлено", "OK");
            }
        }

    }
}
