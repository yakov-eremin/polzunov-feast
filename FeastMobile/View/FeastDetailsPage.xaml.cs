namespace FeastMobile.View;

public partial class FeastDetailsPage : ContentPage
{
	public FeastDetailsPage(FeastDetailsViewModel viewModel)
	{
		InitializeComponent();
		BindingContext = viewModel;
	}

	protected override void OnNavigatedTo(NavigatedToEventArgs args)
	{
        FeastDetailsViewModel viewModel = (FeastDetailsViewModel)BindingContext;
		viewModel.InitializeDataCommand.Execute(null);
  //      CommunityToolkit.Maui.Core.Platform.StatusBar.SetColor(Microsoft.Maui.Graphics.Color.FromHex("FFFFFF"));
		//CommunityToolkit.Maui.Core.Platform.StatusBar.SetStyle(CommunityToolkit.Maui.Core.StatusBarStyle.DarkContent);
        base.OnNavigatedTo(args);
    }
}