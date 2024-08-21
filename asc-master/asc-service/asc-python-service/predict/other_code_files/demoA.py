from torch import nn

from predict.other_code_files.decoder import APFormerHead, APFormerHead2, APFormerHeadSingle, APFormerHeadMulti
from predict.other_code_files.groupmixformer import GroupMixFormer


class A(nn.Module):
    def __init__(self, num_classes: int = 2, backbone_type: str = 'MINIY', decoder_type: str = "APFormerHead2"):
        super().__init__()

        self.encoder, embedding_dims = self.create_encoder(backbone_type.upper())
        if decoder_type == "APFormerHead":
            self.decoder = APFormerHead(in_channels=embedding_dims,
                                        num_classes=num_classes * 16,
                                        feature_strides=[4, 8, 16, 32],
                                        in_index=[0, 1, 2, 3],
                                        decoder_params=dict(
                                            embed_dim=128,
                                            num_heads=[8, 5, 2, 1],
                                            pool_ratio=[1, 2, 4, 8]
                                        ),
                                        dropout_ratio=0.1,
                                        input_transform='multiple_select',
                                        align_corners=False
                                        )
        elif decoder_type == "APFormerHead2":
            self.decoder = APFormerHead2(in_channels=embedding_dims,
                                         num_classes=num_classes * 16,
                                         feature_strides=[4, 8, 16, 32],
                                         in_index=[0, 1, 2, 3],
                                         decoder_params=dict(
                                             embed_dim=128,
                                             num_heads=[8, 5, 2, 1],
                                             pool_ratio=[1, 2, 4, 8]
                                         ),
                                         dropout_ratio=0.1,
                                         input_transform='multiple_select',
                                         align_corners=False
                                         )
        elif decoder_type == "APFormerHeadMulti":
            self.decoder = APFormerHeadMulti(in_channels=embedding_dims,
                                             num_classes=num_classes * 16,
                                             feature_strides=[4, 8, 16, 32],
                                             in_index=[0, 1, 2, 3],
                                             decoder_params=dict(
                                                 embed_dim=128,
                                                 num_heads=[8, 5, 2, 1],
                                                 pool_ratio=[1, 2, 4, 8],
                                                 num_multi=4
                                             ),
                                             dropout_ratio=0.1,
                                             input_transform='multiple_select',
                                             align_corners=False
                                             )
        elif decoder_type == "APFormerHeadSingle":
            self.decoder = APFormerHeadSingle(in_channels=embedding_dims,
                                              num_classes=num_classes * 16,
                                              feature_strides=[4, 8, 16, 32],
                                              in_index=[0, 1, 2, 3],
                                              decoder_params=dict(
                                                  embed_dim=128,
                                                  num_heads=[8, 5, 2, 1],
                                                  pool_ratio=[1, 2, 4, 8]
                                              ),
                                              dropout_ratio=0.1,
                                              input_transform='multiple_select',
                                              align_corners=False
                                              )
        else:
            raise Exception("choose size in [APFormerHead, APFormerHead2, APFormerHeadSingle, APFormerHeadMulti]")

        self.head = nn.Sequential(
            nn.ConvTranspose2d(num_classes * 16, num_classes * 8, kernel_size=4,
                               stride=2, padding=1),
            nn.BatchNorm2d(num_classes * 8),
            nn.GELU(),
            nn.ConvTranspose2d(num_classes * 8, num_classes * 4, kernel_size=3, stride=1, padding=1),
            nn.BatchNorm2d(num_classes * 4),
            nn.GELU(),
            nn.ConvTranspose2d(num_classes * 4, num_classes * 2, kernel_size=4, stride=2, padding=1),
            nn.BatchNorm2d(num_classes * 2),
            nn.GELU(),
            nn.ConvTranspose2d(num_classes * 2, num_classes, kernel_size=3, stride=1, padding=1)
        )

    def create_encoder(self, backbone_type):
        if backbone_type == 'T':
            model = GroupMixFormer(return_interm_layers=True,
                                   embedding_dims=[80, 160, 200, 240],
                                   serial_depths=[4, 4, 12, 4],
                                   mlp_ratios=[4, 4, 4, 4],
                                   drop_path_rate=0.1)
            return model, [80, 160, 200, 240]
        elif backbone_type == 'S':
            model = GroupMixFormer(return_interm_layers=True,
                                   embedding_dims=[80, 160, 320, 320],
                                   serial_depths=[2, 4, 12, 4],
                                   mlp_ratios=[4, 4, 4, 4],
                                   drop_path_rate=0.2)
            return model, [80, 160, 320, 320]
        elif backbone_type == 'B':
            model = GroupMixFormer(return_interm_layers=True,
                                   embedding_dims=[200, 240, 320, 480],
                                   serial_depths=[8, 8, 12, 8],
                                   mlp_ratios=[2, 2, 4, 4],
                                   drop_path_rate=0.5)
            return model, [200, 240, 320, 480]
        elif backbone_type == 'MINIY':
            model = GroupMixFormer(return_interm_layers=True,
                                   embedding_dims=[40, 80, 160, 160],
                                   serial_depths=[3, 3, 12, 4],
                                   mlp_ratios=[4, 4, 4, 4],
                                   drop_path_rate=0.1)
            return model, [40, 80, 160, 160]
        elif backbone_type == 'L':
            model = GroupMixFormer(return_interm_layers=True,
                                   embedding_dims=[240, 320, 360, 480],
                                   serial_depths=[8, 10, 30, 10],
                                   mlp_ratios=[4, 4, 2, 2],
                                   drop_path_rate=0.1)
            return model, [240, 320, 360, 480]
        else:
            raise Exception("choose size in MINIY < T < S < B < L")

    def forward(self, x):
        x = self.encoder(x)
        x = self.decoder(x)
        return self.head(x)
