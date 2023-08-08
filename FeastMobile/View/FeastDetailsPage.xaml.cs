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

        if (viewModel.NumOfImages > 1)
        {
            ImageNumFrame.IsVisible = true;
            ImageNumFrame.IsEnabled = true;
            BackImageButton.IsVisible = true;
            BackImageButton.IsEnabled = true;
            ForwardImageButton.IsVisible = true;
            ForwardImageButton.IsEnabled = true;
        }
        else 
        {
            ImageNumFrame.IsVisible = false;
            ImageNumFrame.IsEnabled = false;
            BackImageButton.IsVisible = false;
            BackImageButton.IsEnabled = false;
            ForwardImageButton.IsVisible = false;
            ForwardImageButton.IsEnabled = false;
        }

        this.Behaviors.Add(new StatusBarBehavior
        {
            StatusBarColor = Colors.White,
            StatusBarStyle = StatusBarStyle.DarkContent
        });
    }
}