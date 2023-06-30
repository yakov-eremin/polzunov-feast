namespace FeastMobile.View;

public partial class EventListPage : ContentPage
{
	public EventListPage(EventViewModel eventVM)
	{
		InitializeComponent();
		BindingContext = eventVM;
    }

    protected override void OnNavigatedTo(NavigatedToEventArgs args)
    {
        ProblemButton.ContentLayout = new Button.ButtonContentLayout(Button.ButtonContentLayout.ImagePosition.Right, 20);
        base.OnNavigatedTo(args);
    }
}