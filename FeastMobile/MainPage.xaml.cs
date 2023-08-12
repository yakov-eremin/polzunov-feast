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
using Microsoft.Maui.Controls.PlatformConfiguration;
using System;
using CommunityToolkit.Maui.Storage;
using SkiaSharp;

using System.Reflection;
using PdfSharpCore.Drawing;
using PdfSharpCore.Pdf;
using iTextSharp.text;

using iTextSharp.text.pdf;
using static Google.Rpc.Context.AttributeContext.Types;
using static System.Net.Mime.MediaTypeNames;

namespace FeastMobile
{
    public partial class MainPage : ContentPage
    {
        private string _deviceToken;
       private PushNotifications pushNotifications = new PushNotifications();
        TokenSender tokenSender = new TokenSender();
        FileSafe fileSafe = new FileSafe();
       
        CancellationTokenSource cancellationTokenSource = new CancellationTokenSource();

        public MainPage(IFileSaver fileSaver)
        {
            InitializeComponent();
            if(Preferences.ContainsKey("DeviceToken")) //проверка генерации нового токена.
            {
                _deviceToken = Preferences.Get("DeviceToken","");
            }
      
            tokenSender.SendTokenToServer(_deviceToken);

            pushNotifications.ReadFireBaseAdminSdk();
            fileSafe.FileSaverInit(fileSaver);
          

        }
       
        private async void OnCounterClicked(object sender, EventArgs e)
        {


        }
    }
}