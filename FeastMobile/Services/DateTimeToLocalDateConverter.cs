using System.Globalization;

namespace FeastMobile.Services;

public class DateTimeToLocalDateConverter : IValueConverter
{
    public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
    {
        return ((DateTime)value).ToString("t", CultureInfo.InvariantCulture);
    }
    public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture)
    {
        return DateTime.Parse(value.ToString());
    }
}
