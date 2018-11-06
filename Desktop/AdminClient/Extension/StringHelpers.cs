using System;

namespace AdminClient
{
    /// <summary>
    /// Extession method cho <see cref="String"/>
    /// </summary>
    public static class StringHelpers
    {
        public static bool IsTime(this string text)
        {
            try
            {
                DateTime time = DateTime.ParseExact(text, "HH:mm", System.Globalization.CultureInfo.CurrentCulture);
            }
            catch (Exception ex)
            {
                return false;
            }

            return true;
        }

        public static bool IsNumber(this string text)
        {
            try
            {
                int temp = int.Parse(text);
            }
            catch(Exception ex)
            {
                return false;
            }

            return true;
        }
    }
}
