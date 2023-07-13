using FeastMobile.View;

namespace FeastMobile.ViewModel;

[QueryProperty(nameof(CurrentEvent), nameof(CurrentEvent))]
public partial class EventDetailsViewModel : BaseViewModel
{
    [ObservableProperty]
    private Event currentEvent;

    [ObservableProperty]
    private string labelText;

    [ObservableProperty]
    private string description;

    public ObservableCollection<string> Images { get; set; }

    public bool IsLabelExpanded { get; set; }

    public EventDetailsViewModel()
    {
        IsLabelExpanded = false;
    }

    [RelayCommand]
    private void InitParamRelatedData()
    {
        Description = CurrentEvent.Description[..250];
        LabelText = "•••";

        Images = new ObservableCollection<string>();
        foreach (var image in CurrentEvent.Images) 
        { 
            Images.Add(image);
        }
        OnPropertyChanged(nameof(Images));
        OnPropertyChanged(nameof(Description));
    }

    [RelayCommand]
    static async Task GoBackAsync()
    {
        await Shell.Current.GoToAsync("..");
    }

    [RelayCommand]
    void ExpandLabel()
    {
        if (CurrentEvent.Description.Length >= 250)
        {
            if (IsLabelExpanded)
            {
                LabelText = "•••";
                Description = CurrentEvent.Description[..250];
                IsLabelExpanded = false;
            }
            else
            {
                LabelText = "▲";
                Description = CurrentEvent.Description[..];
                IsLabelExpanded = true;
            }
        }
        OnPropertyChanged(nameof(Description));
    }

    [RelayCommand]
    async Task GoToRouteSelection()
    {
        await Shell.Current.GoToAsync($"../{nameof(RouteSelectionPage)}");
    }
}
