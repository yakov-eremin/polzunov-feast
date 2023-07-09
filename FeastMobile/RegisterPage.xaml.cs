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

        //Переход на страницу подтверждения
        await Application.Current.MainPage.Navigation.PushAsync(new FinalRegister(), true);
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