using CommunityToolkit.Maui.Behaviors;
using CommunityToolkit.Maui.Core;
using FeastMobile.ViewModel;

namespace FeastMobile.View;

public partial class EventListPage : ContentPage
{
    public EventListPage(EventViewModel eventVM)
    {
        InitializeComponent();
        BindingContext = eventVM;
    }

    [System.Diagnostics.CodeAnalysis.SuppressMessage("Interoperability", "CA1416:Проверка совместимости платформы", Justification = "<Ожидание>")]
    protected override void OnAppearing()
    {
        base.OnAppearing();

        ProblemButton.ContentLayout = new Button.ButtonContentLayout(Button.ButtonContentLayout.ImagePosition.Right, 20);

        EventViewModel viewModel = (EventViewModel)BindingContext;
        viewModel.InitParamRelatedDataCommand.Execute(viewModel);

        this.Behaviors.Add(new StatusBarBehavior
        {
            StatusBarColor = Color.FromArgb("#1E78F0"),
            StatusBarStyle = StatusBarStyle.LightContent
        });


    }
}