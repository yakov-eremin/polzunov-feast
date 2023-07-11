using System.Globalization;

namespace FeastMobile.Services;

public class AddressConverter : IValueConverter
{
    public object Convert(object value, Type targetType, object parameter, CultureInfo cultureInfo)
    {
        return string.Join(" • ", (List<string>)value);
    }
    public object ConvertBack(object value, Type targetType, object parameter, CultureInfo cultureInfo)
    {
        return ((string)value).Split(" • ").ToList();
    }
}
