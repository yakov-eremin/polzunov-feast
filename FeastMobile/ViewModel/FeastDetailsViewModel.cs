using FeastMobile.View;

namespace FeastMobile.ViewModel;

[QueryProperty(nameof(CurrentFeast), nameof(CurrentFeast))]
public partial class FeastDetailsViewModel : BaseViewModel
{
    [ObservableProperty]
    private Feast currentFeast;

    [ObservableProperty]
    private string description;

    [ObservableProperty]
    private string labelText;

    [ObservableProperty]
    private string currentImage;

    [ObservableProperty]
    private int currentImageIndex;

    [ObservableProperty]
    private int numOfImages;

    public bool IsLabelExpanded { get; set; }

    public FeastDetailsViewModel()
    {
        IsLabelExpanded = false;
        InitParamUnrelatedData();
    }

    private void InitParamUnrelatedData()
    {
        CurrentImageIndex = 1;
        LabelText = "•••";
    }

    [RelayCommand]
    private Task InitParamRelatedData()
    {
        Description = CurrentFeast.Description[..700];
        CurrentImage = CurrentFeast.Images[0];
        NumOfImages = CurrentFeast.Images.Count;
        return Task.CompletedTask;
    }

    [RelayCommand]
    static async Task GoBackAsync()
    {
        await Shell.Current.GoToAsync("..");
    }

    [RelayCommand]
    async Task GoToEventsAsync()
    {
        await Shell.Current.GoToAsync($"../{nameof(EventListPage)}",
            new Dictionary<string, object>
            {
                ["CurrentFeast"] = CurrentFeast
            });
    }

    [RelayCommand]
    void ExpandLabel()
    {
        if (CurrentFeast.Description.Length >= 750)
        {
            if (IsLabelExpanded)
            {
                LabelText = "•••";
                Description = CurrentFeast.Description[..700];
                IsLabelExpanded = false;
            }
            else
            {
                LabelText = "▲";
                Description = CurrentFeast.Description[..];
                IsLabelExpanded = true;
            }
        }
    }

    [RelayCommand]
    private void ForwardImage()
    {
        if (CurrentImageIndex == CurrentFeast.Images.Count)
            CurrentImageIndex = 1;
        else
            CurrentImageIndex++;
        CurrentImage = CurrentFeast.Images[CurrentImageIndex - 1];
    }

    [RelayCommand]
    void BackImage()
    {
        if (CurrentImageIndex <= 1)
            CurrentImageIndex = CurrentFeast.Images.Count;
        else
            CurrentImageIndex--;
        CurrentImage = CurrentFeast.Images[CurrentImageIndex - 1];
    }
}
