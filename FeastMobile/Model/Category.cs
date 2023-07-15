using System.Windows.Input;

namespace FeastMobile.Model;

public class Category
{
    
    public string Name { get; set; }
    public string Image { get; set; }

    private bool isSelected;
    public bool IsSelected
    {
        get { return isSelected; }
        set
        {
            if (isSelected != value)
            {
                isSelected = value;
                OnPropertyChanged(nameof(IsSelected));
            }
        }
    }

    public event PropertyChangedEventHandler PropertyChanged;

    protected virtual void OnPropertyChanged(string propertyName)
    {
        PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(propertyName));
    }
    public ICommand ToggleSelectionCommand { get; private set; }

    public Category()
    {
        ToggleSelectionCommand = new Command(ToggleSelection);
    }

    private void ToggleSelection()
    {
        IsSelected = !IsSelected;
    }
}
