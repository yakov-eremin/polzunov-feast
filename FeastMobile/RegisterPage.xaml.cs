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
            await DisplayAlert("������", "��� �� �������", "�K");
            return;
        }

        if (string.IsNullOrEmpty(EmailTextBox.Text))
        {
            await DisplayAlert("������", "����� �� �������", "�K");
            return;
        }

        if (string.IsNullOrEmpty(PassTextBox.Text))
        {
            await DisplayAlert("������", "������ �� ������", "�K");
            return;
        }

        if (!isValidEmail)
        {
            await DisplayAlert("������", "����� �� ������������� �������", "�K");
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
                    name = NameTextBox.Text,
                    email = EmailTextBox.Text
                }),
        Encoding.UTF8,
        "application/json");

        using HttpResponseMessage response = await sharedClient.PostAsync("user/check", jsonContent);

        var jsonResponse = await response.Content.ReadAsStringAsync();

        NameTextBox.IsEnabled = true;
        EmailTextBox.IsEnabled = true;
        PassTextBox.IsEnabled = true;

        if (string.IsNullOrEmpty(jsonResponse))
        {
            //������� �� �������� �������������
            await Application.Current.MainPage.Navigation.PushAsync(new VerifyPage(EmailTextBox.Text, NameTextBox.Text, PassTextBox.Text), true);
        }
        else
        {
            MessageErrorFromServer messageErrorFromServer = JsonSerializer.Deserialize<MessageErrorFromServer>(jsonResponse);
            switch (messageErrorFromServer.message)
            {
                case "Email is already in use":
                    errorLayout.Text = "������ ����� ��� ������������. ��������� �������";
                    break;
                default:
                    errorLayout.Text = "��������� ������. ��������� �������";
                    break;

            }
            errorLayout.IsVisible = true;
        }
    }

    void SetState(bool isValid, Entry entry)
    {
        string visualState = isValid ? "Valid" : "Invalid";
        VisualStateManager.GoToState(entry, visualState);
    }

    private void EmailTextBox_TextChanged(object sender, TextChangedEventArgs e)
    { 
        bool isValid = Regex.IsMatch(e.NewTextValue, @"^([a-zA-Z]|[0-9]|[.]|)*@[a-z]+[.][a-z]+");
        SetState(isValid, EmailTextBox); 
        isValidEmail = isValid;
    }
}