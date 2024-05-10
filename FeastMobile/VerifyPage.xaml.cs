using System.Diagnostics;
using MailKit.Net.Smtp;
using MailKit;
using MimeKit;
using System.Text;
using System.Text.Json;

namespace FeastMobile;

public partial class VerifyPage : ContentPage
{
    public string Email { get; set; }
    public string Password { get; set; }
    public string Name { get; set; }
    public int code { get; set; }

    Random rnd = new Random();

    public VerifyPage(string email, string name, string pass)
	{
		InitializeComponent();

        Email = email;
        Name = name;
        Password = pass;

        WelcomeLabel.Text = $"Мы выслали вам специальный код на {Email}. Если код не пришел, проверьте раздел Спам";
        _ = SendEmailAsync(email);
    }

    private void FirstNum_TextChanged(object sender, TextChangedEventArgs e)
    {
        if (string.IsNullOrEmpty(FirstNum.Text))
        {
            FirstNum.Unfocus();
        } 
        else
        {
            SecondNum.Focus();
        }
    }

    private void SecondNum_TextChanged(object sender, TextChangedEventArgs e)
    {
        if (string.IsNullOrEmpty(SecondNum.Text))
        {
            FirstNum.Focus();
        }
        else
        {
            ThirdNum.Focus();
        }
        
    }

    private void ThirdNum_TextChanged(object sender, TextChangedEventArgs e)
    {
        if (string.IsNullOrEmpty(ThirdNum.Text))
        {
            SecondNum.Focus();
        }
        else
        {
            FourthNum.Focus();
        }
       
    }

    private void FourthNum_TextChanged(object sender, TextChangedEventArgs e)
    {
        if (string.IsNullOrEmpty(FourthNum.Text))
        {
            ThirdNum.Focus();
        }
        else
        {
            verifyNum();
        }
        
    }

    private void Button_Clicked(object sender, EventArgs e)
    {
        verifyNum();
    }

    public async void verifyNum()
    {
        FirstNum.Unfocus();
        SecondNum.Unfocus();
        ThirdNum.Unfocus(); 
        FourthNum.Unfocus();

        if (string.IsNullOrEmpty(FirstNum.Text) || string.IsNullOrEmpty(SecondNum.Text) || string.IsNullOrEmpty(ThirdNum.Text) || string.IsNullOrEmpty(FourthNum.Text))
        {
            await DisplayAlert("Ошибка", "Введен не весь код", "ОK");
            return;
        }

        string enter_code = FirstNum.Text + SecondNum.Text + ThirdNum.Text + FourthNum.Text;

        if (Convert.ToInt32(enter_code) == code)
        {
            HttpClient sharedClient = new()
            {
                BaseAddress = new Uri("http://192.168.0.105:8080"),
            };

            using StringContent jsonContent = new(
                    JsonSerializer.Serialize(new
                    {
                        password = Password,
                        name = Name,
                        email = Email
                    }),
            Encoding.UTF8,
            "application/json");

            using HttpResponseMessage response = await sharedClient.PostAsync("user/signup", jsonContent);


            var jsonResponse = await response.Content.ReadAsStringAsync();

            if (jsonResponse.Contains("accessToken"))
            {
                //Переход на страницу подтверждения
                await Application.Current.MainPage.Navigation.PushAsync(new FinalRegister(), true);
            }
            else
            {
                errorLabel.Text = "Произошла ошибка. Повторите попытку";
                errorLabel.IsVisible = true;
            }
        }
        else
        {
            errorLabel.Text = "Некоректный код. Попробуйте снова";
            errorLabel.IsVisible = true;

            FirstNum.Text = "";
            SecondNum.Text = "";
            ThirdNum.Text = "";
            FourthNum.Text = "";

            FirstNum.Focus();
        }
    }

    public async Task SendEmailAsync(string email)
    {
        code = rnd.Next(999, 10000);

        using var emailMessage = new MimeMessage();

        emailMessage.From.Add(new MailboxAddress("PolzunovFeast", "polzunov.feast@yandex.ru"));
        emailMessage.To.Add(new MailboxAddress("", email));
        emailMessage.Subject = "Подтверждение почты";
        emailMessage.Body = new TextPart(MimeKit.Text.TextFormat.Html)
        {
            Text = "Код подтверждения - " + Convert.ToString(code)
        };

        using (var client = new SmtpClient())
        {
            await client.ConnectAsync("smtp.yandex.ru", 25, false);
            await client.AuthenticateAsync("polzunov.feast@yandex.ru", "uwbrqirwymblzbgx");
            await client.SendAsync(emailMessage);

            await client.DisconnectAsync(true);
        }
    }
}