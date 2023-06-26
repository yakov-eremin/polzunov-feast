namespace FeastMobile.ViewModel;

[QueryProperty(nameof(Feast), "Feast")]
public partial class FeastDetailsViewModel : BaseViewModel
{
    public FeastDetailsViewModel()
    {
        InitializeData();
        IsEditorExpanded = false;
    }

    async void InitializeData()
    {
        Description = Feast.Description;
        EditorExpandedText = "...";
    }

    [ObservableProperty]
    Feast feast;

    [RelayCommand]
    async Task GoBackAsync()
    {
        await Shell.Current.GoToAsync("..");
    }

    [ObservableProperty]
    public string description;

    [ObservableProperty]
    public string editorExpandedText;

    public bool IsEditorExpanded { get; set; }

    [RelayCommand]
    async Task ExpandEditor()
    {
        if (Feast.Description.Length >= 400)
        {
            if (IsEditorExpanded)
            {
                EditorExpandedText = "...";
                Description = Feast.Description[..400];
                IsEditorExpanded = false;
            }
            else
            {
                EditorExpandedText = " ^ ";
                Description = Feast.Description[..];
                IsEditorExpanded = true;
            }
        }
    }
}
