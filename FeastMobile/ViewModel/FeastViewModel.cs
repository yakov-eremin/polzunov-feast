using FeastMobile.Services;
using FeastMobile.View;

namespace FeastMobile.ViewModel;

public partial class FeastViewModel : BaseViewModel
{
    private FeastService feastService;
    public ObservableCollection<Feast> ActualFeasts { get; set; }
    public ObservableCollection<Feast> NotActualFeasts { get; set; }

    [ObservableProperty]
    private bool isRefreshing;

    public FeastViewModel(FeastService feastService)
    {
        this.feastService = feastService;
        InitParamUnrelatedData();
    }

    async void InitParamUnrelatedData()
    {
        var tmpFeasts = await feastService.GetFeastsAsync();

        NotActualFeasts = new ObservableCollection<Feast>();
        ActualFeasts = new ObservableCollection<Feast>();
        foreach (var feast in tmpFeasts)
        { 
            if (feast.Date < DateTime.Today)
                NotActualFeasts.Add(feast);
            else
                ActualFeasts.Add(feast);
        }
        OnPropertyChanged(nameof(NotActualFeasts));
        OnPropertyChanged(nameof(ActualFeasts));
    }

    [RelayCommand]
    async Task GetFeastsAsync()
    {
        if (IsBusy)
            return;

        try
        {
            IsBusy = true;
            var tmpFeasts = await feastService.GetFeastsAsync();

            NotActualFeasts = new ObservableCollection<Feast>();
            ActualFeasts = new ObservableCollection<Feast>();
            foreach (var feast in tmpFeasts)
            {
                if (feast.Date > DateTime.Today)
                    NotActualFeasts.Add(feast);
                else
                    ActualFeasts.Add(feast);
            }

        }
        catch (Exception ex)
        {
            Debug.WriteLine($"Unable to get events: {ex.Message}");
            await Shell.Current.DisplayAlert("Error!", ex.Message, "OK");
        }
        finally
        {
            IsBusy = false;
            IsRefreshing = false;
        }
    }

    [RelayCommand]
    async Task GoToDetailsAsync(Feast feast)
    {
        if (feast is null)
            return;

        await Shell.Current.GoToAsync($"{nameof(FeastDetailsPage)}",
            new Dictionary<string, object>
            {
                ["CurrentFeast"] = feast
            });
    }

    [RelayCommand]
    async Task GoToEventsAsync(Feast feast)
    {
        if (feast is null)
            return;

        await Shell.Current.GoToAsync($"{nameof(EventListPage)}",
            new Dictionary<string, object>
            {
                ["CurrentFeast"] = feast
            });
    }
}
