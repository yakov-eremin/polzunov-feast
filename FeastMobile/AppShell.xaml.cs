using FeastMobile.View;

namespace FeastMobile
{
    public partial class AppShell : Shell
    {
        public AppShell()
        {
            InitializeComponent();

            Routing.RegisterRoute(nameof(FeastDetailsPage), typeof(FeastDetailsPage));
        }
    }
}