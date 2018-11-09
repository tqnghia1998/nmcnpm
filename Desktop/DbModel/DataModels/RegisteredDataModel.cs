using Microsoft.EntityFrameworkCore;
using System.ComponentModel.DataAnnotations;

namespace DbModel
{
    /// <summary>
    /// Bảng thời gian củ thể của từng học phần trong tuần
    /// </summary>
    public class RegisteredDataModel
    {
        /// <summary>
        /// Mã số sinh viên
        /// </summary>
        [Required]
        public string Mssv { get; set; }

        /// <summary>
        /// Id môn học
        /// </summary>
        [Required]
        public string Id { get; set; }

        /// <summary>
        /// Thứ trong tuần
        /// </summary>
        [Required]
        public string DayInTheWeek { get; set; }

        /// <summary>
        /// Tên môn học
        /// </summary>
        [MaxLength(256)]
        public string Subject { get; set; }

        public RegisteredDataModel() { }

        public RegisteredDataModel(string mssv, string id, string dayintheweek, string subject)
        {
            Mssv = mssv;
            Id = id;
            DayInTheWeek = dayintheweek;
            Subject = subject;
        }
    }
}
