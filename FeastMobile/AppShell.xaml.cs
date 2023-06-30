using FeastMobile.View;

namespace FeastMobile
{
    public partial class AppShell : Shell
    {
        public AppShell()
        {
            InitializeComponent();

            Routing.RegisterRoute(nameof(FeastDetailsPage), typeof(FeastDetailsPage));
            Routing.RegisterRoute(nameof(EventListPage), typeof(EventListPage));
            Routing.RegisterRoute(nameof(FiltersPage), typeof(FiltersPage));
        }
    }
}