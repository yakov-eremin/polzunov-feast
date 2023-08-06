using System.Text.Json;
using System.Text;
using System.Text.RegularExpressions;

namespace FeastMobile;

public partial class RegisterPage : ContentPage
{
    bool isValidEmail = false;

	public RegisterPage()
	{
		InitializeComponent();
        SetState(false, EmailTextBox);
	}

    private void LoginClick(object sender, EventArgs e)
    {
        Application.Current.MainPage.Navigation.PushAsync(new LoginPage(), true);

    }
    private async void RegisterClick(object sender, EventArgs e)
    {
        errorLayout.IsVisible = false;

        if (string.IsNullOrEmpty(NameTextBox.Text))
        {
            await DisplayAlert("Ошибка", "Имя не введено", "ОK");
            return;
        }

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

        if (!isValidEmail)
        {
            await DisplayAlert("Ошибка", "Почта не соответствует формату", "ОK");
            return;
        }

        NameTextBox.IsEnabled = false;
        EmailTextBox.IsEnabled = false; 
        PassTextBox.IsEnabled = false;

        HttpClient sharedClient = new()
        {
            BaseAddress = new Uri("http://192.168.0.105:8080"),
        };

        using StringContent jsonContent = new(
                JsonSerializer.Serialize(new
                {
                    username = NameTextBox.Text,
                    password = PassTextBox.Text,
                    name = NameTextBox.Text,
                    email = EmailTextBox.Text
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
            MessageErrorFromServer messageErrorFromServer = JsonSerializer.Deserialize<MessageErrorFromServer>(jsonResponse);
            switch (messageErrorFromServer.message)
            {
                case "Email is not unique":
                    errorLayout.Text = "Данная почта уже используется. Повторите попытку";
                    break;
                default:
                    errorLayout.Text = "Произошла ошибка. Повторите попытку";
                    break;

            }
            errorLayout.IsVisible = true;

            EmailTextBox.IsEnabled = true;
            PassTextBox.IsEnabled = true;
            NameTextBox.IsEnabled = true;


        }
    }

    void SetState(bool isValid, Entry entry)
    {
        string visualState = isValid ? "Valid" : "Invalid";
        VisualStateManager.GoToState(entry, visualState);
    }

    private void EmailTextBox_TextChanged(object sender, TextChangedEventArgs e)
    { 
        bool isValid = Regex.IsMatch(e.NewTextValue, @"^([a-zA-Z]|[0-9])*@[a-z]+.[a-z]+");
        SetState(isValid, EmailTextBox); 
        isValidEmail = isValid;
    }
}