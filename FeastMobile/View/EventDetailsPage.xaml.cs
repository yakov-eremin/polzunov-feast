using CommunityToolkit.Maui.Behaviors;
using CommunityToolkit.Maui.Core;

namespace FeastMobile.View;

public partial class EventDetailsPage : ContentPage
{
	public EventDetailsPage(EventDetailsViewModel viewModel)
	{
		InitializeComponent();
		BindingContext = viewModel;
    }

    [System.Diagnostics.CodeAnalysis.SuppressMessage("Interoperability", "CA1416:Проверка совместимости платформы", Justification = "<Ожидание>")]
    protected override void OnAppearing()
    {
        base.OnAppearing();

        EventDetailsViewModel viewModel = (EventDetailsViewModel)BindingContext;

        viewModel.InitParamRelatedDataCommand.Execute(viewModel);

        this.Behaviors.Add(new StatusBarBehavior
        {
            StatusBarColor = Colors.White,
            StatusBarStyle = StatusBarStyle.DarkContent
        });

    }
}