namespace FeastMobile.View;

public partial class FeastListPage : ContentPage
{
	public FeastListPage(FeastViewModel feastVM)
	{
		InitializeComponent();
		this.BindingContext = feastVM;
    }
}