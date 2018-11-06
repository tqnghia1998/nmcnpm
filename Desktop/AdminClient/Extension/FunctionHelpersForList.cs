using System;
using System.Collections.ObjectModel;
using System.Windows.Controls;

namespace AdminClient
{
    /// <summary>
    /// Các hàm bổ trợ cho việc tạo các danh sách item
    /// </summary>
    public static class FunctionHelpersForList
    {
        public static ObservableCollection<string> GenerateTimeToChoose()
        {
            ObservableCollection<string> items = new ObservableCollection<string>();
            int minutes = 0;
            string hour;
            string minute;

            for (int i = 0; i < 48; ++i)
            {
                hour = (minutes / 60).ToString().Length < 2 ? $"0{minutes / 60}" : $"{minutes / 60 }";
                minute = (minutes % 60).ToString().Length < 2 ? $"0{minutes % 60}" : $"{minutes % 60 }";

                items.Add($"{hour}:{minute}");
                minutes += 30;
            }

            return items;
        }

        public static ObservableCollection<string> GenerateCourse(int start, int end)
        {
            ObservableCollection<string> items = new ObservableCollection<string>();

            for (int i = start; i < end; ++i)
            {
                items.Add($"{i}-{i + 1}");

            }

            return items;
        }
    }
}
