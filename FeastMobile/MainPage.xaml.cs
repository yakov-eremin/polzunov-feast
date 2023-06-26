using System.Diagnostics;

namespace FeastMobile
{
    public partial class MainPage : ContentPage
    {
        int count = 0;

        public MainPage()
        {
            InitializeComponent();
        }

        private void LoginClick(object sender, EventArgs e)
        {
            Application.Current.MainPage.Navigation.PushModalAsync(new LoginPage(), true);

        }
    }
}