using CommunityToolkit.Mvvm.Messaging;
using FirebaseAdmin;
using FirebaseAdmin.Messaging;
using Google.Apis.Auth.OAuth2;
using Newtonsoft.Json;
using System.Text;
using FirebaseAdmin.Auth;
using Push_server_app;
using System.Reflection.Metadata;
using Google.Apis.Auth.OAuth2;
using Firebase.Database;
using Firebase.Database.Query;

using System;
using System.Net.Sockets;
using System.Net;


//Данные для проверки отправки уведомлений по времени
DateTime feastDate = new DateTime(2023, 08, 06, 01, 33 , 00);
DateTime currentDate = DateTime.Now;
string token;
var tokenList = new List<string>();
FireBaseServices services = new FireBaseServices();
TimeSpan checkTime;
object tokenListLock = new object();

//Инициализация Firebase Admin Sdk.
services.ReadFireBaseAdminSdk();

// Создание экземпляра HttpListener для прослушивания входящих HTTP запросов на указанном порту (80)
HttpListener listener = new HttpListener();
listener.Prefixes.Add($"http://+:80/");
listener.Start();
Console.WriteLine("Сервер запущен. Ожидание запросов...");

// Поток для отправки уведомлений по времени (тестовый вариант)
Task.Run(async () =>
{
    while (true)
    {
        HttpListenerContext context = listener.GetContext();

        using (Stream body = context.Request.InputStream)
        {
            using (StreamReader reader = new StreamReader(body, context.Request.ContentEncoding))
            {
                string path = context.Request.Url.AbsolutePath;
                if (path == "/api/message" && context.Request.HttpMethod == "POST")
                {
                    // Получаем JSON из тела запроса
                    string jsonRequestBody = reader.ReadToEnd();

                    try
                    {
                        // Десериализация JSON в объект MessageRequest
                        var requestData = JsonConvert.DeserializeObject<JsonMessage>(jsonRequestBody);

                        // Проверяем, что данные не пустые
                       

                        // Используем значения из requestData и помещаем их в отдельные переменные
                        string deviceToken = requestData.Token;
                        string title = requestData.Title;
                        string text = requestData.Text;

                        Console.WriteLine(deviceToken);
                        Console.WriteLine(title);
                        Console.WriteLine(text);

                        List <string> a = new List<string>();
                        a.Add(deviceToken);
                        // В этом месте можно выполнить необходимые действия с полученными данными,
                        // например, сохранить их в базу данных или выполнить какую-то логику

                        // Создаем объект с параметрами
                        services.setNotificationParams(tokenList,title,text);
                        services.SendNotification().Wait();
                    }
                    catch (Exception ex)
                    {
                        // В случае ошибки десериализации или другой ошибки возвращаем статус 500
                        context.Response.StatusCode = (int)HttpStatusCode.InternalServerError;
                        context.Response.Close();
                    }

                }
                else
                {
                    Console.WriteLine("Некорректный запрос");
                }
            }
        }
    }
});

// Прием входящих HTTP запросов (новых токенов) и их обработка;
// Отправка уведомления при выполнении события (добавлен новый пользователь)
while (true)
{
    HttpListenerContext context = listener.GetContext();

    using (Stream body = context.Request.InputStream)
    {
        using (StreamReader reader = new StreamReader(body, context.Request.ContentEncoding))
        {
            string path = context.Request.Url.AbsolutePath;
            if (path == "/api/token" && context.Request.HttpMethod == "POST")
            {
                token = reader.ReadToEnd();
                Console.WriteLine("Полученный токен: " + token);
                lock (tokenListLock)
                {
                    if (!tokenList.Contains(token))
                    {
                        tokenList.Add(token);
                        string title = "Ура!!!!";
                        string messageBody = "У нас новый пользователь!!!!";
                        services.setNotificationParams(tokenList, title, messageBody);
                        services.SendNotification().Wait();
                    }
                }
            }
            else
            {
                Console.WriteLine("Некорректный запрос");
            }
        }
    }
}

/*Тестовый пример структуры для отправки уведомлений*/
struct JsonMessage
{
    public string Token { get; set; }
    public string Text { get; set; } 
    public string Title { get; set; }       
}

