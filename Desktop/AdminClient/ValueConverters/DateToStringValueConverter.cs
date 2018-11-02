using System;
using System.Globalization;
using System.Windows.Controls;

namespace AdminClient
{
    /// <summary>
    /// 
    /// </summary>
    public class DateToStringValueConverter : BaseValueConverter<DateToStringValueConverter>
    {
        public override object Convert(object value, Type targetType, object parameter, CultureInfo culture)
        {
            return null;
        }

        public override object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture)
        {
            string dateString;
            try
            {
                dateString = ((DateTime)value).ToString("MM/dd/yyyy");
            }
            catch
            {
                return string.Empty;
            }

            return dateString;
        }
    }
}
