using FeastMobile.Services;
using FeastMobile.View;

namespace FeastMobile.ViewModel;

[QueryProperty(nameof(Feast), nameof(Feast))]
public partial class FeastDetailsViewModel : BaseViewModel
{
    [ObservableProperty]
    Feast feast;

    public FeastDetailsViewModel()
    {
        IsEditorExpanded = false;
    }

    [RelayCommand]
    async Task InitializeData()
    {
        Description = Feast.Description[..350];
        CurrentImage = Feast.Images[0];
        CurrentImageIndex = 1;
        EditorExpandedText = "...";
        NumOfImages = Feast.Images.Count;
    }

    [RelayCommand]
    async Task GoBackAsync()
    {
        await Shell.Current.GoToAsync("..");
    }

    [RelayCommand]
    async Task GoToEventsAsync()
    {


        await Shell.Current.GoToAsync($"../{nameof(EventListPage)}",
            new Dictionary<string, object>
            {
                ["Feast"] = Feast
            });
    }

    [ObservableProperty]
    public string description;

    [ObservableProperty]
    public string editorExpandedText;

    [ObservableProperty]
    public string currentImage;

    [ObservableProperty]
    public int currentImageIndex;

    [ObservableProperty]
    public int numOfImages;

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

    [RelayCommand]
    async Task ForwardImage()
    {
        if (CurrentImageIndex == Feast.Images.Count)
            CurrentImageIndex = 1;
        else
            CurrentImageIndex++;
        CurrentImage = Feast.Images[CurrentImageIndex-1];
    }

    [RelayCommand]
    async Task BackImage()
    {
        if (CurrentImageIndex <= 1)
            CurrentImageIndex = Feast.Images.Count;
        else
            CurrentImageIndex--;
        CurrentImage = Feast.Images[CurrentImageIndex-1];
    }
}
