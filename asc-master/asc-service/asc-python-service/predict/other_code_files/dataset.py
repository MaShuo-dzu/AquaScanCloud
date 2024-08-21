import os

import torch
from PIL import Image
from torchvision import transforms


class TestDataset(torch.utils.data.Dataset):
    def __init__(self, images_dir):
        self.transform = transforms.Compose([
            transforms.ToTensor(),
            transforms.Normalize([0.485, 0.456, 0.406], [0.229, 0.224, 0.225])
        ])

        self.ids = os.listdir(images_dir)
        self.images_fps = [os.path.join(images_dir, image_id) for image_id in self.ids]

    def __getitem__(self, i):
        image_path = self.images_fps[i]
        name = os.path.splitext(os.path.basename(image_path))[0]
        image = Image.open(self.images_fps[i])

        image = self.transform(image)
        return image, name

    def __len__(self):
        return len(self.ids)


class DictDataset(torch.utils.data.Dataset):
    def __init__(self, feature_dict: dict):
        self.transform = transforms.Compose([
            transforms.ToTensor(),
            transforms.Normalize([0.485, 0.456, 0.406], [0.229, 0.224, 0.225])
        ])

        self.feature_dict = feature_dict
        self.feature_keys_list = [i for i in feature_dict.keys()]

    def __getitem__(self, i):
        key = self.feature_keys_list[i]
        image = self.feature_dict[key]
        image = self.transform(image)
        return image, key

    def __len__(self):
        return len(self.feature_keys_list)
