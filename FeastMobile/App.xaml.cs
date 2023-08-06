namespace FeastMobile
{
    public partial class App : Application
    {
        public App()
        {
            InitializeComponent();

            MainPage = new AppShell();

            Routing.RegisterRoute(nameof(LoginPage), typeof(LoginPage));
            Routing.RegisterRoute(nameof(RegisterPage), typeof(RegisterPage));
            Routing.RegisterRoute(nameof(VerifyPage), typeof(VerifyPage));
            Routing.RegisterRoute(nameof(FinalRegister), typeof(FinalRegister));
            Routing.RegisterRoute(nameof(FinalLogin), typeof(FinalLogin));
        }
    }
}