using Microsoft.Extensions.Logging;

namespace FeastMobile
{
    public static class MauiProgram
    {
        public static MauiApp CreateMauiApp()
        {
            var builder = MauiApp.CreateBuilder();
            builder
                .UseMauiApp<App>()
                .UseMauiCommunityToolkit()
                .ConfigureFonts(fonts =>
                {
                    fonts.AddFont("Roboto-Bold.ttf", "RobotoBold");
                    fonts.AddFont("Roboto-Medium.ttf", "RobotoMedium");
                    fonts.AddFont("Roboto-Regular.ttf", "RobotoRegular");
                    fonts.AddFont("OpenSans-Regular.ttf", "OpenSansRegular");
                    fonts.AddFont("OpenSans-Semibold.ttf", "OpenSansSemibold");
                })
                .UseMauiMaps();

#if DEBUG
		builder.Logging.AddDebug();
#endif
            builder.Services.AddSingleton<FeastService>();
            builder.Services.AddSingleton<EventLoader>();

            builder.Services.AddSingleton<FeastViewModel>();
            builder.Services.AddSingleton<FeastDetailsViewModel>();
            builder.Services.AddSingleton<EventViewModel>();
            builder.Services.AddSingleton<EventDetailsViewModel>();
            builder.Services.AddSingleton<RouteSelectionPage>();

            builder.Services.AddTransient<FeastListPage>();
            builder.Services.AddTransient<FeastDetailsPage>();
            builder.Services.AddTransient<EventListPage>();
            builder.Services.AddTransient<EventDetailsPage>();
            builder.Services.AddTransient<FiltersPage>();
            builder.Services.AddTransient<RouteSelectionPage>();

            return builder.Build();
        }
    }
}