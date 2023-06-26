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
		base.OnNavigatedTo(args);
	}
}