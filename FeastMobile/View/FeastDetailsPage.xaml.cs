using CommunityToolkit.Maui.Behaviors;
using CommunityToolkit.Maui.Core;

namespace FeastMobile.View;

public partial class FeastDetailsPage : ContentPage
{
	public FeastDetailsPage(FeastDetailsViewModel viewModel)
	{
		InitializeComponent();
		BindingContext = viewModel;
    }

    [System.Diagnostics.CodeAnalysis.SuppressMessage("Interoperability", "CA1416:Проверка совместимости платформы", Justification = "<Ожидание>")]
    protected override void OnAppearing()
	{
        base.OnAppearing();

        FeastDetailsViewModel viewModel = (FeastDetailsViewModel)BindingContext;
        viewModel.InitParamRelatedDataCommand.Execute(viewModel);

        this.Behaviors.Add(new StatusBarBehavior
        {
            StatusBarColor = Colors.White,
            StatusBarStyle = StatusBarStyle.DarkContent
        });
    }
}