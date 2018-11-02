using System;
using System.Globalization;
using System.Windows;

namespace AdminClient
{
    /// <summary>
    /// Chuyển <see cref="bool"/> sang <see cref="Visibility"/>
    /// </summary>
    public class BooleanToVisibilityConverter : BaseValueConverter<BooleanToVisibilityConverter>
    {
        public override object Convert(object value, Type targetType, object parameter, CultureInfo culture)
        {
            if (null == parameter)
            {
                return (bool)value ? Visibility.Hidden : Visibility.Visible;
            }
            else
            {
                return (bool)value ? Visibility.Visible : Visibility.Hidden;
            }
        }

        public override object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture)
        {
            throw new NotImplementedException();
        }
    }
}
