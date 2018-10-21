using System;
using System.Globalization;
using System.Windows.Data;
using System.Windows.Markup;

namespace AdminClient
{
    /// <summary>
    /// Base value converter cho các value converter khác kế thừa
    /// </summary>
    /// <typeparam name="T">The type of this value converter</typeparam>
    public abstract class BaseValueConverter<T> : MarkupExtension, IValueConverter
        where T : class, new()
    {
        #region Private Members
        
        private static T mConverter = null;

        #endregion

        #region Markup Extension Methods

        /// <summary>
        /// Cung cấp một instance tĩnh của value converter
        /// </summary>
        /// <param name="serviceProvider">The service provider</param>
        /// <returns></returns>
        public override object ProvideValue(IServiceProvider serviceProvider)
        {
            return mConverter ?? (mConverter = new T());
        }

        #endregion

        #region Value converter methods

        /// <summary>
        /// Phương thức chuyển một kiểu dự liệu nhận được sang kiểu dữ liệu khác
        /// </summary>
        /// <param name="value"></param>
        /// <param name="targetType"></param>
        /// <param name="parameter"></param>
        /// <param name="culture"></param>
        /// <returns></returns>
        public abstract object Convert(object value, Type targetType, object parameter, CultureInfo culture);

        /// <summary>
        /// Ngược lại hàm bên trên
        /// </summary>
        /// <param name="value"></param>
        /// <param name="targetType"></param>
        /// <param name="parameter"></param>
        /// <param name="culture"></param>
        /// <returns></returns>
        public abstract object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture);

        #endregion
    }
}
