using System.Text.Json;
using System.Text;
using System.Linq;

namespace FeastMobile;

public partial class LoginPage : ContentPage
{
	public LoginPage()
	{
		InitializeComponent();
	}

    private async void LoginButton_Clicked(object sender, EventArgs e)
    {
        errorLayout.IsVisible = false;

        if (string.IsNullOrEmpty(EmailTextBox.Text))
        {
            await DisplayAlert("Ошибка", "Почта не введена", "ОK");
            return;
        }

        if (string.IsNullOrEmpty(PassTextBox.Text))
        {
            await DisplayAlert("Ошибка", "Пароль не введен", "ОK");
            return;
        }

        EmailTextBox.IsEnabled = false;
        PassTextBox.IsEnabled = false;

        HttpClient sharedClient = new()
        {
            BaseAddress = new Uri("http://192.168.0.105:8080"),
        };
        
        using StringContent jsonContent = new(
                JsonSerializer.Serialize(new
                {
                    //username поменять на почту, когда на сервере починят
                    username = EmailTextBox.Text,
                    password = PassTextBox.Text
                }),
        Encoding.UTF8,
        "application/json");

        using HttpResponseMessage response = await sharedClient.PostAsync("user/signin", jsonContent);

        var jsonResponse = await response.Content.ReadAsStringAsync();

        if (jsonResponse.Contains("accessToken"))
        {
            //Переход на страницу подтверждения
            await Application.Current.MainPage.Navigation.PushAsync(new FinalLogin(), true);
        }
        else
        {
            MessageErrorFromServer messageErrorFromServer = JsonSerializer.Deserialize<MessageErrorFromServer>(jsonResponse);
            switch (messageErrorFromServer.message)
            {
                case "Wrong password":
                    errorLayout.Text = "Неправильный пароль. Повторите попытку";
                    break;
                //Пока не работает, скоро на сервере исправят и будет почта вместо логина
                case "Username not found":
                    errorLayout.Text = "Такого пользователя не существует. Повторите попытку";
                    break;
                default:
                    errorLayout.Text = "Произошла ошибка. Повторите попытку";
                    break;

            }
            errorLayout.IsVisible = true;

            EmailTextBox.IsEnabled = true;
            PassTextBox.IsEnabled = true;

           
            

        }
    }
}