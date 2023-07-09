using System.Diagnostics;

namespace FeastMobile
{
    public partial class MainPage : ContentPage
    {
        public MainPage()
        {
            InitializeComponent();
        }

        private void LoginClick(object sender, EventArgs e)
        {
            Application.Current.MainPage.Navigation.PushAsync(new LoginPage(), true);

        }

        private void RegisterClick(object sender, EventArgs e)
        {
            Application.Current.MainPage.Navigation.PushAsync(new RegisterPage(), true);
        }
    }
}