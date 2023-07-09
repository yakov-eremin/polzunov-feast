using CommunityToolkit.Mvvm.Messaging;
using FirebaseAdmin;
using FirebaseAdmin.Messaging;
using Google.Apis.Auth.OAuth2;
using Newtonsoft.Json;
using System.Text;
using FirebaseAdmin.Auth;
using Google.Cloud.Firestore;
using Firebase;
using Google.Cloud.Firestore;
using Firebase.Database;
using Firebase.Database.Query;
using FirebaseAdmin;
using FirebaseAdmin.Messaging;
using Google.Apis.Auth.OAuth2;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Maui;
using Microsoft.Maui.Controls;
using Microsoft.Maui.Controls.PlatformConfiguration.WindowsSpecific;
using Application = Microsoft.Maui.Controls.Application;
using System.Net;

namespace FeastMobile
{
    public partial class MainPage : ContentPage
    {
        private string _deviceToken;
       private PushNotifications pushNotifications = new PushNotifications();
        public MainPage()
        {
            InitializeComponent();
            if(Preferences.ContainsKey("DeviceToken")) //проверка генерации нового токена.
            {
                _deviceToken = Preferences.Get("DeviceToken","");
            }
           
            pushNotifications.ReadFireBaseAdminSdk();
           
        }
       
        private async void OnCounterClicked(object sender, EventArgs e) /*Проверка работоспособности пушей*/
        {
            if(checkBox.IsChecked==true)
            {
               TokenSender tokenSender = new TokenSender();
                tokenSender.SendTokenToServer(_deviceToken);
            }
            else
            {
                App.Current.MainPage.DisplayAlert("Уведомления запрещены", "Вы запретили отпревку уведомлений", "OK");
            }
           
        }
    }
}