using FeastMobile.Services;
using FeastMobile.View;

namespace FeastMobile.ViewModel;

public partial class FeastViewModel : BaseViewModel
{
    private FeastService feastService;
    public ObservableCollection<Feast> Feasts { get; set; }

    public FeastViewModel(FeastService feastService)
    {
        this.feastService = feastService;
        InitializeData();
    }

    async void InitializeData()
    {
        var tmpFeasts = await feastService.GetFeastsAsync();

        Feasts = new ObservableCollection<Feast>();
        foreach (var feast in tmpFeasts)
            Feasts.Add(feast);

        OnPropertyChanged(nameof(Feasts));
    }

    [ObservableProperty]
    bool isRefreshing;

    [RelayCommand]
    async Task GetFeastsAsync()
    {
        if (IsBusy)
            return;

        try
        {
            IsBusy = true;
            var feasts = await feastService.GetFeastsAsync();

            if (Feasts.Count != 0)
                Feasts.Clear();

            foreach (var feast in feasts)
                Feasts.Add(feast);

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

        await Shell.Current.GoToAsync($"{nameof(FeastDetailsPage)}", true, 
            new Dictionary<string, object>
            {
                {"Feast", feast}
            });
    }
}
