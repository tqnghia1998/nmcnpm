using Microsoft.EntityFrameworkCore;
using System.ComponentModel.DataAnnotations;

namespace DbModel
{
    /// <summary>
    /// Bảng thời gian củ thể của từng học phần trong tuần
    /// </summary>
    public class ScheduleDataModel
    {
        /// <summary>
        /// Mã học phần
        /// </summary>
        public string Id { get; set; }

        /// <summary>
        /// Ngày trong tuần
        /// </summary>
        public string DayInTheWeek { get; set; }

        /// <summary>
        /// Lớp học
        /// </summary>
        public string Room { get; set; }

        /// <summary>
        /// Tiết 
        /// </summary>
        public string Period { get; set; }

        /// <summary>
        /// Thời gian bắt đầu
        /// </summary>
        public string TimeStart { get; set; }

        /// <summary>
        /// Thời gian kết thúc
        /// </summary>
        public string TimeFinish { get; set; }
    }
}
