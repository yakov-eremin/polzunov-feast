using CommunityToolkit.Maui.Views;
using FeastMobile.Services;
using FeastMobile.View;
using Microsoft.Maui.Handlers;
using Microsoft.Maui.Controls.Compatibility.Platform.Android;

namespace FeastMobile.View;

public partial class EventTimeFilter : Popup
{
    public delegate void SelectedTimeChangedEventHandler(TimeSpan selectedTimeFrom, TimeSpan selectedTimeTo);
    public event SelectedTimeChangedEventHandler SelectedTimeChanged;
    public TimeSpan SelectedTimeFrom { get; private set; }
    public TimeSpan SelectedTimeTo { get; private set; }
    

    private TimePicker TimePickerFrom; // Переименована переменная

    private TimePicker TimePickerTo;
    public EventTimeFilter()
	{
		InitializeComponent();

        timePickerFrom.Time = new TimeSpan(0, 0, 0); // Установите значение времени по умолчанию для "От"
        timePickerTo.Time = new TimeSpan(23, 59, 0); // Установите значение времени по умолчанию для "После"

    }
    public void TimePickerFrom_PropertyChanged(object sender, PropertyChangedEventArgs e)
    {
        SelectedTimeFrom = ((TimePicker)sender).Time;
        Console.WriteLine(((TimePicker)sender).Time.ToString());

        var selectedTime = SelectedTimeFrom.ToString("hh\\:mm");
        TimeFromLabel.Text = selectedTime;
    }

    public void TimePickerTo_PropertyChanged(object sender, PropertyChangedEventArgs e)
    {
        SelectedTimeTo = ((TimePicker)sender).Time;
        //Console.WriteLine(((TimePicker)sender).Time.ToString());

        var selectedTime = SelectedTimeTo.ToString("hh\\:mm");
        TimeToLabel.Text = selectedTime;
    }

    private void SavePopupButton_Clicked(object sender, EventArgs e)
    {
        Console.WriteLine(SelectedTimeFrom.ToString());
        SelectedTimeChanged?.Invoke(SelectedTimeFrom, SelectedTimeTo);
        Close();
    }
    private void timeFromFrame_Tapped(object sender, EventArgs e)
    {

        timePickerFrom.IsEnabled = true;
        timePickerFrom.IsVisible = false;
       
        var handler = timePickerFrom.Handler as ITimePickerHandler;
        handler.PlatformView.PerformClick();


        //Console.WriteLine("hhhhhhhhhhhhhhhhhh");
    }
    private void timeToFrame_Tapped(object sender, EventArgs e)
    {

        timePickerTo.IsEnabled = true;
        timePickerTo.IsVisible = false;

        var handler = timePickerTo.Handler as ITimePickerHandler;
        handler.PlatformView.PerformClick();


        //Console.WriteLine("hhhhhhhhhhhhhhhhhh");
    }
    private void ClosePopupButton_Clicked(object sender, EventArgs e)
    {
        Close();
    }
}