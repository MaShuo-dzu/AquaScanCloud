import os
import time

import numpy as np
import torch
from PIL import Image, ImageFile
from torch.utils.data import DataLoader
from torchvision.transforms import transforms

from predict.other_code_files import TestDataset, DictDataset
from predict.other_code_files.demoA import A

ImageFile.LOAD_TRUNCATED_IMAGES = True
Image.MAX_IMAGE_PIXELS = None


def delete_folder(path):
    try:
        # 列出目录下的所有文件和子目录
        files = os.listdir(path)

        for file in files:
            # 构建文件或子目录的完整路径
            full_path = os.path.join(path, file)

            if os.path.isdir(full_path):
                # 如果是子目录，递归调用删除函数
                delete_folder(full_path)
            else:
                # 如果是文件，直接删除
                os.remove(full_path)

        # 删除空目录
        os.rmdir(path)
        print(f"Deleted folder: {path}")

    except Exception as e:
        print(f"Error deleting folder {path}: {e}")


def pad_image(image_tensor: Image, target_size, original_width, original_height):
    # 计算目标宽度和高度
    target_width = (original_width + target_size - 1) // target_size * target_size
    target_height = (original_height + target_size - 1) // target_size * target_size

    # 计算需要填充的宽度和高度
    pad_width = max(0, target_width - original_width)
    pad_height = max(0, target_height - original_height)

    # 计算填充量
    pad_top = pad_height // 2
    pad_left = pad_width // 2

    # 创建一个填充后的 Tensor
    padded_image = np.full((target_height, target_width, 3), 0, dtype=np.uint8)

    # 将原始图像放入填充后的 Tensor 的中心位置
    padded_image[pad_top:pad_top + original_height, pad_left:pad_left + original_width, :] = image_tensor

    return padded_image, target_width, target_height, pad_top, pad_left


def slice_image(image_array, slice_size, height, width, save: bool = True, temp: str = ''):
    # 计算切片的行数和列数
    rows = height // slice_size
    cols = width // slice_size

    # 用于保存切片图像的字典
    sliced_images = {}
    # 循环切割图像
    for i in range(rows):
        for j in range(cols):
            # 计算切片的坐标
            top = i * slice_size
            left = j * slice_size
            bottom = top + slice_size
            right = left + slice_size

            # 切割图像
            slice_array = image_array[top:bottom, left:right, :]

            if save:
                img_path = os.path.join(temp, f"{i}_{j}.tif")
                pil_image = Image.fromarray(slice_array)
                pil_image.save(img_path)
            else:
                # 将切片图像张量存入字典
                sliced_images[(i, j)] = slice_array

    return sliced_images


def create_mask(sliced_images_dict: dict, model, device, save: bool = True, temp: str = ''):
    if save:
        dataset = TestDataset(temp)
        dataloader = DataLoader(
            dataset=dataset,
            shuffle=False,
            batch_size=3,
            num_workers=4,
            drop_last=False
        )
        for image, name in dataloader:
            output = model(image.to(device))
            output = torch.nn.functional.softmax(output, dim=1)
            output = torch.argmax(output, dim=1).cpu()

            for i in range(len(name)):
                tensor_np = np.array(output[i])
                array_data = tensor_np.astype(np.uint8)
                pred = Image.fromarray(array_data)
                pred.save(os.path.join(temp, name[i] + "_maskadd.tif"))

            del output
            torch.cuda.empty_cache()

    else:
        dataset = DictDataset(sliced_images_dict)
        dataloader = DataLoader(
            dataset=dataset,
            shuffle=False,
            batch_size=16,
            num_workers=4,
            drop_last=False
        )
        for image, key in dataloader:
            output = model(image.to(device))
            output = torch.nn.functional.softmax(output, dim=1)
            output = torch.argmax(output, dim=1).cpu()

            for i in range(len(key[0])):
                sliced_images_dict[(int(key[0][i]), int(key[1][i]))] = output[i]

            del output
            torch.cuda.empty_cache()


def reconstruct_image(sliced_images_dict, slice_size, save: bool = True, temp: str = ''):
    if save:
        mask_path_list = []
        max_row = -1
        max_col = -1
        for i in os.listdir(temp):
            if "maskadd" in i:
                mask_path_list.append(os.path.join(temp, i))
                row, col, _ = os.path.splitext(i)[0].split("_")
                row, col = int(row), int(col)
                if row > max_row:
                    max_row = row
                if col > max_col:
                    max_col = col

        # 计算重构后图像的尺寸
        height = (max_row + 1) * slice_size
        width = (max_col + 1) * slice_size

        # 创建一个零填充的张量，用于重构图像
        reconstructed_image = torch.zeros((height, width))

        for each_mask in mask_path_list:
            row, col, _ = os.path.splitext(os.path.basename(each_mask))[0].split("_")
            row, col = int(row), int(col)
            mask = Image.open(each_mask)
            to_tensor = transforms.ToTensor()
            mask = to_tensor(mask) * 255

            top = row * slice_size
            left = col * slice_size
            bottom = top + slice_size
            right = left + slice_size

            # 将切片图像张量放入相应位置
            reconstructed_image[top:bottom, left:right] = mask

        return reconstructed_image

    else:
        # 获取字典中的最大行列索引
        max_row = max(key[0] for key in sliced_images_dict.keys())
        max_col = max(key[1] for key in sliced_images_dict.keys())

        # 计算重构后图像的尺寸
        height = (max_row + 1) * slice_size
        width = (max_col + 1) * slice_size

        # 创建一个零填充的张量，用于重构图像
        reconstructed_image = torch.zeros((height, width))

        # 遍历字典，将切片图像张量放入相应位置
        for key, value in sliced_images_dict.items():
            row, col = key
            top = row * slice_size
            left = col * slice_size
            bottom = top + slice_size
            right = left + slice_size

            # 将切片图像张量放入相应位置
            reconstructed_image[top:bottom, left:right] = value

        return reconstructed_image


def Inhibition(matrix, target_number: int = 1):
    # 获取矩阵的行数
    num_rows = matrix.size(0)

    # 遍历每一行
    for i in range(num_rows):
        # 获取当前行
        row = matrix[i, :]

        # 计算目标数字在当前行中的出现次数
        occurrences = torch.sum(row == target_number).item()

        # 计算占比
        total_elements = row.numel()
        percentage = occurrences / total_elements

        if percentage > 0.6:
            matrix[i, :].zero_()

    num_cols = matrix.size(1)

    # 遍历每一列
    for i in range(num_cols):
        # 获取当前行
        col = matrix[:, i]

        # 计算目标数字在当前行中的出现次数
        occurrences = torch.sum(col == target_number).item()

        # 计算占比
        total_elements = col.numel()
        percentage = occurrences / total_elements

        if percentage > 0.6:
            matrix[:, i].zero_()

    return matrix


def save_tif(output_path: str, tensor):
    tensor_np = np.array(tensor)
    # contains_only_0_and_1 = np.all(np.logical_or(tensor_np == 0, tensor_np == 1))
    # print(contains_only_0_and_1)
    array_data = tensor_np.astype(np.uint8)
    pred = Image.fromarray(array_data)
    pred.save(output_path)


def predict(to_pred_dir, result_save_path):
    """

    :param to_pred_dir: 预测图像的路径
    :param result_save_path: 结果保存的路径
    :return:
    """
    py_path = os.path.abspath(__file__)
    model_dir = os.path.dirname(py_path)
    # save temp file
    save = True
    temp = os.path.join(model_dir, "temp")
    if save:
        if not os.path.exists(temp):
            os.mkdir(temp)
        else:
            delete_folder(temp)
            os.mkdir(temp)

    # device
    device = torch.device("cuda" if torch.cuda.is_available() else "cpu")

    start_time = time.time()

    # model
    # model = RSModelPlus(num_classes=2, convT=True).to(device)
    # model = URSModel(num_classes=2).to(device)

    # model.load_state_dict(torch.load(os.path.join(model_dir, r"best_remodel_f1.pth"), map_location=device))
    model = A(backbone_type="B", decoder_type="APFormerHead").to(device)

    model.load_state_dict(torch.load(os.path.join(model_dir, r"remodel.pth"), map_location=device))

    model.eval()

    pred_imgs_paths = os.listdir(to_pred_dir)
    pred_img_path = os.path.join(to_pred_dir, pred_imgs_paths[0])  # ! 测试集只有一张图片

    # init
    print("init")
    target_size = 480
    image = Image.open(pred_img_path).convert("RGB")
    original_width, original_height = image.size

    # padding
    print("padding")
    image_array, target_width, target_height, pad_top, pad_left = pad_image(image, target_size, original_width,
                                                                            original_height)

    # 调用函数进行图像切片
    print("调用函数进行图像切片")
    sliced_images_dict: dict = slice_image(image_array, target_size, target_height, target_width, save, temp)

    # 处理
    print("处理")
    # 如果save == true，则生成的 mask 会保存成 xxx_maskadd.tif 的图片
    create_mask(sliced_images_dict, model, device, save, temp)

    # 调用函数重构图像
    print("调用函数重构图像")
    reconstructed_image = reconstruct_image(sliced_images_dict, target_size, save, temp)

    # 去padding
    print("去padding")
    image_tensor = reconstructed_image[pad_top:pad_top + original_height, pad_left:pad_left + original_width]

    # # 噪声抑制
    # print("噪声抑制")
    # image_tensor = Inhibition(image_tensor)

    # 保存成tif
    print("保存成tif")
    save_tif(result_save_path, image_tensor)
    end_time = time.time()

    print("time cost: ", end_time - start_time)
