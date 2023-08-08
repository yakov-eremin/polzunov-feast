using System.Globalization;

namespace FeastMobile.Services;

public class CategoryListToStringConverter : IValueConverter
{
    public object Convert(object value, Type targetType, object parameter, CultureInfo cultureInfo)
    {
        List<Category> categories = (List<Category>)value;
        List<string> categoryNames = new List<string>();
        foreach (Category category in categories)
            categoryNames.Add(category.Name);

        return string.Join(" • ", categoryNames);
    }

    public object ConvertBack(object value, Type targetType, object parameter, CultureInfo cultureInfo)
    {
        return ((string)value).Split(" • ").ToList();
    }
}
