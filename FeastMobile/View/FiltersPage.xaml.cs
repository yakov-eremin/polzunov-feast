using CommunityToolkit.Maui.Behaviors;
using CommunityToolkit.Maui.Core;

namespace FeastMobile.View;

public partial class FiltersPage : ContentPage
{

    public FiltersPage()
    {
        InitializeComponent();
        BindingContext = new FiltersPageViewModel();
    }

    public FiltersPage(FiltersPageViewModel viewModel)
    {
        InitializeComponent();
        BindingContext = viewModel;
    }
    private void CategoryButton_Clicked(object sender, EventArgs e)
    {
        var imageButton = (Button)sender;
        var selectedCategory = (Category)imageButton.BindingContext;
       
        var viewModel = (FiltersPageViewModel)BindingContext;

        if (viewModel.SelectedCategories.Contains(selectedCategory))
        {
            // ��������� ��� �������, ������� ������� ��������� ������ � ������� ��������� �� ������ ���������
            imageButton.BackgroundColor = Color.FromHex("#FFFFFF");
            imageButton.BorderColor = Color.FromHex("#C5C5C5");

            viewModel.RemoveSelectedCategory(selectedCategory);
        }
        else
        {
            // ��������� �� �������, ������� ��������� ��������� ������ � ��������� ��������� � ������ ���������
            imageButton.BackgroundColor = Color.FromHex("#F2F7FE");
            imageButton.BorderColor = Color.FromHex("#1E78F0");
            viewModel.SelectedCategories.Add(selectedCategory);
        }

        //// ��������� ����� � Label, ����� ���������� ������ ��������� ���������
        //SelectedCategoriesLabel.Text = string.Join(", ", viewModel.SelectedCategoryNames);
    }

    [System.Diagnostics.CodeAnalysis.SuppressMessage("Interoperability", "CA1416:�������� ������������� ���������", Justification = "<��������>")]
    protected override void OnAppearing()
    {
        base.OnAppearing();

        FiltersPageViewModel viewModel = (FiltersPageViewModel)BindingContext;
        viewModel.InitParamRelatedDataCommand.Execute(viewModel);

        this.Behaviors.Add(new StatusBarBehavior
        {
            StatusBarColor = Colors.White,
            StatusBarStyle = StatusBarStyle.DarkContent
        });
    }
}