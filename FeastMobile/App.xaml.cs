namespace FeastMobile
{
    public partial class App : Application
    {
        public App()
        {
            InitializeComponent();

            MainPage = new AppShell();

            Routing.RegisterRoute(nameof(LoginPage), typeof(LoginPage));

        }
    }
}