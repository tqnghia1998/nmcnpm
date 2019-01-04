using System;
using System.Collections.Generic;
using System.Text;

namespace DbModel
{
    /// <summary>
    /// Thông tin chi tiết các môn học 
    /// </summary>
    public class ListSubRegDTO
    {

        /// <summary>
        /// Id môn học
        /// </summary>
        public string Id { get; set; }

        /// <summary>
        /// Tên môn học
        /// </summary>
        public string Subject { get; set; }

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

        /// <summary>
        /// Tên khoa
        /// </summary>
        public string Major { get; set; }

        /// <summary>
        /// Số tín chỉ
        /// </summary>
        public int Credit { get; set; }

        /// <summary>
        /// Giảng viên phụ trách
        /// </summary>
        public string Teacher { get; set; }

        /// <summary>
        /// Term of this subject
        /// </summary>
        public string Term { get; set; }

        /// <summary>
        /// Course of this subject
        /// </summary>
        public string Course { get; set; }

        /// <summary>
        /// Thời gian bắt đầu học phần
        /// </summary>
        public string StartOfCourse { get; set; }

        /// <summary>
        /// Thời gian kết thúc học phần
        /// </summary>
        public string FinishOfCourse { get; set; }

        public ListSubRegDTO() { }
    }
}

