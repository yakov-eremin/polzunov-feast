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
            await DisplayAlert("������", "����� �� �������", "�K");
            return;
        }

        if (string.IsNullOrEmpty(PassTextBox.Text))
        {
            await DisplayAlert("������", "������ �� ������", "�K");
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
                    //username �������� �� �����, ����� �� ������� �������
                    username = EmailTextBox.Text,
                    password = PassTextBox.Text
                }),
        Encoding.UTF8,
        "application/json");

        using HttpResponseMessage response = await sharedClient.PostAsync("user/signin", jsonContent);

        var jsonResponse = await response.Content.ReadAsStringAsync();

        if (jsonResponse.Contains("accessToken"))
        {
            //������� �� �������� �������������
            await Application.Current.MainPage.Navigation.PushAsync(new FinalLogin(), true);
        }
        else
        {
            MessageErrorFromServer messageErrorFromServer = JsonSerializer.Deserialize<MessageErrorFromServer>(jsonResponse);
            switch (messageErrorFromServer.message)
            {
                case "Wrong password":
                    errorLayout.Text = "������������ ������. ��������� �������";
                    break;
                //���� �� ��������, ����� �� ������� �������� � ����� ����� ������ ������
                case "Username not found":
                    errorLayout.Text = "������ ������������ �� ����������. ��������� �������";
                    break;
                default:
                    errorLayout.Text = "��������� ������. ��������� �������";
                    break;

            }
            errorLayout.IsVisible = true;

            EmailTextBox.IsEnabled = true;
            PassTextBox.IsEnabled = true;

           
            

        }
    }
}