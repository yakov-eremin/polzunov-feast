using Microsoft.Extensions.Logging;
using CommunityToolkit.Maui;
using FeastMobile.View;
using FeastMobile.Services;

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
                });

#if DEBUG
		builder.Logging.AddDebug();
#endif
            builder.Services.AddSingleton<FeastService>();

            builder.Services.AddSingleton<FeastViewModel>();
            builder.Services.AddTransient<FeastDetailsViewModel>();

            builder.Services.AddSingleton<FeastListPage>();
            builder.Services.AddTransient<FeastDetailsPage>();

            return builder.Build();
        }
    }
}