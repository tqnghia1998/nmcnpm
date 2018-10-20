using System;
using System.ComponentModel.DataAnnotations;

namespace DbModel
{
    /// <summary>
    /// 
    /// </summary>
    public class SubjectDataModel
    {
        /// <summary>
        /// Mã học phần phân biệt các học phần giống nhau khác lớp 
        /// và giữa các học phần khác
        /// </summary>
        [Key]
        public string Id { get; set; }

        /// <summary>
        /// Tên khoa
        /// </summary>
        [Required]
        [MaxLength(256)]
        public string Major { get; set; }

        /// <summary>
        /// Tên học phần
        /// </summary>
        [Required]
        [MaxLength(256)]
        public string Subject { get; set; }

        /// <summary>
        /// Giảng viên phụ trách
        /// </summary>
        public string Teacher { get; set; }

        /// <summary>
        /// Phòng học
        /// </summary>
        public string Place { get; set; }

        /// <summary>
        /// Trạng thái
        /// </summary>
        public byte Status { get; set; } = (byte)StatusModel.NonActive;
    }
}
