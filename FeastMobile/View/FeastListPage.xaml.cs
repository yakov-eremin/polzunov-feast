using CommunityToolkit.Maui.Behaviors;
using CommunityToolkit.Maui.Core;

namespace FeastMobile.View;

public partial class FeastListPage : ContentPage
{
	public FeastListPage(FeastViewModel feastVM)
	{
		InitializeComponent();
		this.BindingContext = feastVM;

        Binding binding = new Binding { Source = feastVM, Path = "ActualFeasts", Mode = BindingMode.Default };
        EventsCollectionView.SetBinding(CollectionView.ItemsSourceProperty, binding);
    }


    [System.Diagnostics.CodeAnalysis.SuppressMessage("Interoperability", "CA1416:Проверка совместимости платформы", Justification = "<Ожидание>")]
    protected override void OnAppearing()
    {
        base.OnAppearing();
        this.Behaviors.Add(new StatusBarBehavior
        {
            StatusBarColor = Color.FromArgb("#1E78F0"),
            StatusBarStyle = StatusBarStyle.LightContent
        });

        UpcomingFeastsRadioButton.IsChecked = true;
    }


    void OnRadioButtonCheckedChanged(object sender, CheckedChangedEventArgs e)
    {
        if (UpcomingFeastsRadioButton is not null && PastFeastsRadioButton is not null)
        {
            if (UpcomingFeastsRadioButton.IsChecked)
            {
                Binding binding = new Binding { Source = this.BindingContext, Path = "ActualFeasts", Mode = BindingMode.Default };
                EventsCollectionView.SetBinding(CollectionView.ItemsSourceProperty, binding);
            }
            else
            {
                Binding binding = new Binding { Source = this.BindingContext, Path = "NotActualFeasts", Mode = BindingMode.Default };
                EventsCollectionView.SetBinding(CollectionView.ItemsSourceProperty, binding);
            }
        }
    }
}